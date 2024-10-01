package com.globallogic.newkitchensink.service;

import com.globallogic.newkitchensink.controllers.MemberDTO;
import com.globallogic.newkitchensink.controllers.MemberEvent;
import com.globallogic.newkitchensink.controllers.Operation;
import com.globallogic.newkitchensink.model.Member;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberWithJpaTemplateService {

    private final MongoTemplate memberTemplate;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final SequenceGeneratorService sequenceGeneratorService;

    private final ModelMapper modelMapper = new ModelMapper();

    @Value("${db.prepopulate}")
    private boolean prePopulate;

    @Autowired
    public MemberWithJpaTemplateService(MongoTemplate memberTemplate,
                                        ApplicationEventPublisher applicationEventPublisher,
                                        SequenceGeneratorService sequenceGeneratorService) {
        this.memberTemplate = memberTemplate;
        this.applicationEventPublisher = applicationEventPublisher;
        this.sequenceGeneratorService = sequenceGeneratorService;
    }

    @PostConstruct
    public void init() {
        if (prePopulate) {
            //'John Smith', 'john.smith@mailinator.com', '2125551212'
            Member initial = new Member("John Smith", "john.smith@mailinator.com", "2125551212", true);
            initial.setId(sequenceGeneratorService.generateSequence(Member.ID_SEQUENCE_NAME));
            memberTemplate.save(initial);
        }
    }

    //Activated Transaction even if for now only one dml is applied, may be useful on enhancing the app
    @Transactional
    public MemberDTO createMember(MemberDTO memberDto) {
        Member toBeCreated = modelMapper.map(memberDto, Member.class);
        toBeCreated.setVisible(true);
        toBeCreated.setId(sequenceGeneratorService.generateSequence(Member.ID_SEQUENCE_NAME));
        Member created = memberTemplate.save(toBeCreated);
        applicationEventPublisher.publishEvent(new MemberEvent(this, Operation.CREATED, created));
        return modelMapper.map(created, MemberDTO.class);
    }

    @Transactional
    public MemberDTO updateMember(MemberDTO memberDto) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(memberDto.getId()));
        Member toBeUpdated = memberTemplate.findOne(query,Member.class);
        if (toBeUpdated == null) {
            throw new EntityNotFoundException();
        }
        modelMapper.map(memberDto, toBeUpdated);
        Member updated = memberTemplate.save(toBeUpdated);
        applicationEventPublisher.publishEvent(new MemberEvent(this, Operation.UPDATED, updated));
        return modelMapper.map(updated, MemberDTO.class);
    }

    public MemberDTO getMember(Long id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        Member found = memberTemplate.findOne(query,Member.class);
        if (found == null) {
            throw new EntityNotFoundException();
        }
        return modelMapper.map(found, MemberDTO.class);
    }

    public List<MemberDTO> getMemberList() {
        List<Member> foundList = memberTemplate.findAll(Member.class);
        return foundList.stream().filter(e -> e.isVisible()).map(e -> modelMapper.map(e, MemberDTO.class)).collect(Collectors.toList());
    }

}
