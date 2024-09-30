package com.globallogic.newkitchensink.service;

import com.globallogic.newkitchensink.controllers.MemberDTO;
import com.globallogic.newkitchensink.controllers.MemberEvent;
import com.globallogic.newkitchensink.controllers.Operation;
import com.globallogic.newkitchensink.model.Member;
import com.globallogic.newkitchensink.repository.MemberRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final ModelMapper modelMapper = new ModelMapper();

    @Value("${db.prepopulate}")
    private boolean prePopulate;

    @Autowired
    public MemberService(MemberRepository memberRepository,
     ApplicationEventPublisher applicationEventPublisher) {
        this.memberRepository = memberRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @PostConstruct
    public void init() {
        if (prePopulate) {
            //'John Smith', 'john.smith@mailinator.com', '2125551212'
            Member initial = new Member("John Smith", "john.smith@mailinator.com", "2125551212", true);
            memberRepository.save(initial);
        }
    }

    @Transactional
    public MemberDTO createMember(MemberDTO memberDto) {
        Member toBeCreated = modelMapper.map(memberDto, Member.class);
        toBeCreated.setVisible(true);
        Member created = memberRepository.save(toBeCreated);
        applicationEventPublisher.publishEvent(new MemberEvent(this, Operation.CREATED, created));
        return modelMapper.map(created, MemberDTO.class);
    }

    @Transactional
    public MemberDTO updateMember(MemberDTO memberDto) {
        Member toBeUpdated = memberRepository.getReferenceById(memberDto.getId());
        modelMapper.map(memberDto, toBeUpdated);
        Member updated = memberRepository.save(toBeUpdated);
        applicationEventPublisher.publishEvent(new MemberEvent(this, Operation.UPDATED, updated));
        return modelMapper.map(updated, MemberDTO.class);
    }

    public MemberDTO getMember(Long id) {
        Member found = memberRepository.getReferenceById(id);
        return modelMapper.map(found, MemberDTO.class);
    }

    public List<MemberDTO> getMemberList() {
        List<Member> foundList = memberRepository.findAll();
        return foundList.stream().filter(e -> e.isVisible()).map(e -> modelMapper.map(e, MemberDTO.class)).collect(Collectors.toList());
    }

}
