package Collabo.MoITZY.web.controller;

import Collabo.MoITZY.domain.Inform;
import Collabo.MoITZY.dto.InformDto;
import Collabo.MoITZY.dto.ResponseDto;
import Collabo.MoITZY.web.service.InformService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class InformController {

    private final InformService informService;

    @GetMapping("/mo-itzy/main")
    public ResponseDto<?> getInforms(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return informService.getAllInforms(pageable);
    }
}

