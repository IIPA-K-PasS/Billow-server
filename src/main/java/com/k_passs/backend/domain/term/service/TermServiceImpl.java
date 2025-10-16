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

        // 1. 필수 약관 ID 목록을 DB에서 조회
        Set<Integer> requiredTermIds = termRepository.findAllRequiredTermIds();

        // 2. 요청된 모든 약관을 DB에서 조회하여 Map 형태로 변환 (ID -> Term 엔티티)
        Map<Integer, Term> allTerms = termRepository.findByIdIn(receivedTermIds).stream()
                .collect(Collectors.toMap(Term::getId, Function.identity()));

        // 3. 존재하지 않는 약관 ID 검증
        if (allTerms.size() != receivedTermIds.size()) {
            // 요청된 ID 개수와 조회된 Term 엔티티 개수가 다르면, 존재하지 않는 약관 ID가 포함됨
            throw new GeneralException(ErrorStatus.TERM_NOT_FOUND);
        }

        // 4. 필수 약관 동의 여부 검증 및 누락된 필수 약관 체크
        for (Integer requiredId : requiredTermIds) {
            // 필수 약관이 요청에 포함되었는지, 그리고 동의했는지 확인
            boolean isAgreed = request.getTerms().stream()
                    .filter(termReq -> termReq.getTermId().equals(requiredId))
                    .findFirst()
                    .map(TermRequestDTO.TermAgreement::getAgreed)
                    .orElse(false); // 요청에 포함되지 않았으면 동의하지 않은 것으로 간주

            if (!isAgreed) {
                // 필수 약관 미동의 또는 누락 시 에러
                throw new GeneralException(ErrorStatus.REQUIRED_TERM_NOT_AGREED);
            }
        }

        // 5. 약관 동의 정보 저장 로직
        List<UserTerm> userTermsToSave = request.getTerms().stream()
                .map(termReq -> {
                    Term term = allTerms.get(termReq.getTermId());

                    return UserTerm.builder()
                            .user(user)
                            .term(term)
                            .agreed(termReq.getAgreed())
                            .build();
                })
                .toList();

        userTermRepository.saveAll(userTermsToSave);

        // 6. 응답 DTO 변환 및 반환
        return TermConverter.toTermAgreeResult(user.getId(), request);
    }
}
