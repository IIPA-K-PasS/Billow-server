package com.k_passs.backend.domain.term.service;

import com.k_passs.backend.domain.term.converter.TermConverter;
import com.k_passs.backend.domain.term.dto.request.TermRequestDTO;
import com.k_passs.backend.domain.term.dto.response.TermResponseDTO;
import com.k_passs.backend.domain.term.entity.Term;
import com.k_passs.backend.domain.term.entity.TermRepository;
import com.k_passs.backend.domain.term.entity.UserTerm;
import com.k_passs.backend.domain.term.entity.UserTermRepository;
import com.k_passs.backend.domain.user.entity.User;
import com.k_passs.backend.global.error.code.status.ErrorStatus;
import com.k_passs.backend.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TermServiceImpl implements TermService{

    private final TermRepository termRepository; // 약관 Repository
    private final UserTermRepository userTermRepository; // 사용자 약관 Repository

    // [추가] 전체 약관 목록 및 사용자 동의 여부 조회
    @Override
    @Transactional(readOnly = true)
    public TermResponseDTO.GetAgreeResult getAllTerms(User user) {
        // 1. 모든 약관(Term) 목록 조회
        List<Term> allTerms = termRepository.findAll();

        // 2. 해당 사용자의 모든 약관 동의 정보(UserTerm) 조회
        // (가정: UserTermRepository에 findByUser(User user) 메소드가 존재)
        List<UserTerm> userTerms = userTermRepository.findByUser(user);

        // 3. UserTerm 리스트를 Map<Term ID, Agreed Status> 형태로 변환 (조회 효율을 위해)
        Map<Integer, Boolean> userAgreements = userTerms.stream()
                .collect(Collectors.toMap(
                        userTerm -> userTerm.getTerm().getId(), // UserTerm.term.id
                        UserTerm::getAgreed // UserTerm.agreed
                ));

        // 4. Converter를 사용하여 응답 DTO로 변환
        return TermConverter.toGetAllTermsResult(allTerms, userAgreements);
    }

    @Override
    @Transactional
    public TermResponseDTO.TermAgreeResult termAgree(User user, TermRequestDTO.TermAgreeRequest request) {
        Set<Integer> receivedTermIds = request.getTerms().stream()
                .map(TermRequestDTO.TermAgreement::getTermId)
                .collect(Collectors.toSet());

        // 필수 약관 ID 목록 조회
        Set<Integer> requiredTermIds = termRepository.findAllRequiredTermIds();

        // 요청된 약관들을 DB에서 조회 (ID → Term)
        Map<Integer, Term> allTerms = termRepository.findByIdIn(receivedTermIds).stream()
                .collect(Collectors.toMap(Term::getId, Function.identity()));

        // 존재하지 않는 약관 검증
        if (allTerms.size() != receivedTermIds.size()) {
            throw new GeneralException(ErrorStatus.TERM_NOT_FOUND);
        }

        // 필수 약관 동의 여부 검증
        Map<Integer, Boolean> receivedAgreements = request.getTerms().stream()
                .collect(Collectors.toMap(
                        TermRequestDTO.TermAgreement::getTermId,
                        TermRequestDTO.TermAgreement::getAgreed
                ));

        for (Integer requiredId : requiredTermIds) {
            if (!receivedAgreements.getOrDefault(requiredId, false)) {
                throw new GeneralException(ErrorStatus.REQUIRED_TERM_NOT_AGREED);
            }
        }

        for (TermRequestDTO.TermAgreement termReq : request.getTerms()) {
            Term term = allTerms.get(termReq.getTermId());

            userTermRepository.findByUserAndTerm(user, term)
                    .ifPresentOrElse(
                            existing -> {
                                // 이미 존재하면 동의 상태만 갱신
                                existing.updateAgreed(termReq.getAgreed());
                            },
                            () -> {
                                // 없으면 새로 생성
                                UserTerm newUserTerm = UserTerm.builder()
                                        .user(user)
                                        .term(term)
                                        .agreed(termReq.getAgreed())
                                        .build();
                                userTermRepository.save(newUserTerm);
                            }
                    );
        }

        return TermConverter.toTermAgreeResult(user.getId(), request);
    }
}
