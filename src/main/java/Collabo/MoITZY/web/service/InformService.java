package Collabo.MoITZY.web.service;

import Collabo.MoITZY.domain.Inform;
import Collabo.MoITZY.web.repository.InformRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InformService {

    private final InformRepository informRepository;

    public List<Inform> getAllInforms() {
        return informRepository.findAll();
    }
}