package com.example.demo.services;

import com.example.demo.models.VoteType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.repositories.VoteTypeRepository;

import java.util.List;

@Service
public class VoteTypeService {

    private final VoteTypeRepository voteTypeRepository;
    @Autowired
    public VoteTypeService(VoteTypeRepository voteTypeRepository) {
        this.voteTypeRepository = voteTypeRepository;
    }

    public void addVoteType(VoteType voteType){
        voteTypeRepository.save(voteType);
    }

    public void deleteVoteType(Integer id) {
        boolean exists = voteTypeRepository.existsById(id);
        if (!exists) {
            throw new IllegalStateException("VoteType with Id " + id + " does not exist");
        } else {
            voteTypeRepository.deleteById(id);
        }
    }
    public List<VoteType> getVoteTypes(){
       return voteTypeRepository.findAll();
    }
}
