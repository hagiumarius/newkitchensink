package com.globallogic.newkitchensink.controllers;

import com.globallogic.newkitchensink.service.MemberWithJpaTemplateService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberWithJpaTemplateService memberService;

    @Autowired
    public MemberController(MemberWithJpaTemplateService memberService) {
        this.memberService = memberService;
    }

    @PostMapping(produces="application/json", consumes="application/json")
    public ResponseEntity<MemberDTO> createMember(@RequestBody @Valid MemberDTO memberDTO) {
        MemberDTO created = memberService.createMember(memberDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping(value="/{id}", produces="application/json", consumes="application/json")
    public ResponseEntity<MemberDTO> updateMember(@PathVariable Long id, @RequestBody @Valid MemberDTO memberDTO) {
        memberDTO.setId(id);//in case id is not present or is having a different value
        MemberDTO updated = memberService.updateMember(memberDTO);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @GetMapping(value="/{id}", produces="application/json")
    public ResponseEntity<MemberDTO> getMember(@PathVariable Long id) {
        return new ResponseEntity<>(memberService.getMember(id), HttpStatus.OK);
    }

    @GetMapping(produces="application/json")
    public ResponseEntity<List<MemberDTO>> getMemberList() {
        return new ResponseEntity<>(memberService.getMemberList(), HttpStatus.OK);
    }

}
