package com.k_passs.backend.domain.term.service;

import com.k_passs.backend.domain.term.dto.request.TermRequestDTO;
import com.k_passs.backend.domain.term.dto.response.TermResponseDTO;
import com.k_passs.backend.domain.user.entity.User;

public interface TermService {
    TermResponseDTO.TermAgreeResult termAgree(User user, TermRequestDTO.TermAgreeRequest request);
}
