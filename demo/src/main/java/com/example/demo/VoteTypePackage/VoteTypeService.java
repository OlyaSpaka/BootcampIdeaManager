package com.example.demo.VoteTypePackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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
}
