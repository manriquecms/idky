package org.manriquecms.mafia.controller;

import org.manriquecms.mafia.model.Member;
import org.manriquecms.mafia.service.MafiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/mafia")
public class MafiaController {

    @Autowired
    MafiaService mafiaService;

    @RequestMapping(method = RequestMethod.POST, value = "/member")
    public String addMember(@RequestParam("member") Member member){
        mafiaService.addMember(member);
        return "OK";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/members", produces = {"application/JSON"})
    public List<Member> addMember(){
        return mafiaService.getMembers();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/generate/family", produces = {"application/JSON"})
    public List<Member> generateRandomFamily(@RequestParam("levels") int levels, @RequestParam("maxSubordinates") int maxSubordinates){
        for(int i = 1 ; i <= levels ; i++){

        }

        return mafiaService.getMembers();
    }

}
