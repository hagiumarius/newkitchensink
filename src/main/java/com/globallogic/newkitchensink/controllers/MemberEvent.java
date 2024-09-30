package com.globallogic.newkitchensink.controllers;

import com.globallogic.newkitchensink.model.Member;
import org.springframework.context.ApplicationEvent;

public class MemberEvent extends ApplicationEvent {

    private Member member;

    private Operation operation;

    public MemberEvent(Object source, Operation operation, Member member) {
        super(source);
        this.member = member;
        this.operation = operation;
    }

    public Member getMember() {
        return member;
    }

    public Operation getOperation() {
        return operation;
    }
}
