package Collabo.MoITZY.web.controller;

import Collabo.MoITZY.domain.Inform;
import Collabo.MoITZY.dto.InformDto;
import Collabo.MoITZY.web.service.InformService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class InformController {

    private final InformService informService;

    @GetMapping("/mo-itzy/main")
    public List<InformDto> getInforms() {
        return informService.getAllInforms();
    }
}

