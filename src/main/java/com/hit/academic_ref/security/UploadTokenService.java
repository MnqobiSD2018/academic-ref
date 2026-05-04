package com.hit.academic_ref.security;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.hit.academic_ref.entity.Project;
import com.hit.academic_ref.entity.UploadToken;
import com.hit.academic_ref.repository.ProjectRepository;
import com.hit.academic_ref.repository.UploadTokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UploadTokenService {
 
    private final UploadTokenRepository tokenRepository;
    private final ProjectRepository projectRepository;

    // Admin calls this to generate a link
    public UploadToken createToken(Long projectId, String studentEmail, int validForDays) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new RuntimeException("Project not found"));

        UploadToken token = new UploadToken();
        token.setToken(UUID.randomUUID().toString()); // e.g. "a3f8c2d1-..."
        token.setProject(project);
        token.setIssuedToEmail(studentEmail);
        token.setCreatedAt(LocalDateTime.now());
        token.setExpiresAt(LocalDateTime.now().plusDays(validForDays));
        token.setUsed(false);

        return tokenRepository.save(token);
    }

    // Called before every upload to validate the token
    public UploadToken validateToken(String tokenString) {
        UploadToken token = tokenRepository.findByToken(tokenString)
            .orElseThrow(() -> new RuntimeException("Invalid upload link"));

        if (token.isUsed()) {
            throw new RuntimeException("This upload link has already been used");
        }

        if (LocalDateTime.now().isAfter(token.getExpiresAt())) {
            throw new RuntimeException("This upload link has expired");
        }

        return token;
    }

    // Admin can extend the deadline
    public UploadToken extendToken(Long tokenId, int additionalDays) {
        UploadToken token = tokenRepository.findById(tokenId)
            .orElseThrow(() -> new RuntimeException("Token not found"));
        token.setExpiresAt(token.getExpiresAt().plusDays(additionalDays));
        return tokenRepository.save(token);
    }

    // Admin can revoke a link immediately
    public void revokeToken(Long tokenId) {
        UploadToken token = tokenRepository.findById(tokenId)
            .orElseThrow(() -> new RuntimeException("Token not found"));
        token.setExpiresAt(LocalDateTime.now().minusMinutes(1)); // set to past
        tokenRepository.save(token);
    }

    public List<UploadToken> getAllTokens() {
        return tokenRepository.findAll();
    }

    // expose save for callers that modify tokens
    public UploadToken save(UploadToken token) {
        return tokenRepository.save(token);
    }

}
