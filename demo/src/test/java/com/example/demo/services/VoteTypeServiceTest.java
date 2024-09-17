package com.example.demo.services;

import com.example.demo.models.Role;
import com.example.demo.models.VoteType;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.VoteTypeRepository;
import lombok.Cleanup;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class VoteTypeServiceTest {


    @Autowired
    private VoteTypeService voteTypeService;

    @Autowired
    private VoteTypeRepository voteTypeRepository;
    private VoteType voteTypeTest;

    @BeforeEach
    void setUp() {
        voteTypeTest = new VoteType();
        voteTypeTest.setName("TestName");
        voteTypeTest.setPoints(5);

        voteTypeRepository.save(voteTypeTest);

    }

    @Test
    void testAddVoteType(){
        //Given
       VoteType voteTypeNew = new VoteType();
       voteTypeNew.setName("NewName");
       voteTypeNew.setPoints(10);

        //When
        voteTypeService.addVoteType(voteTypeNew);
        //Then
        List<VoteType> voteTypes = voteTypeRepository.findAll();
        assertThat(voteTypes).hasSize(2);
        assertThat(voteTypes).extracting(VoteType::getName).contains("NewName");
    }

    @Test
    void testDeleteVoteTypeWhenExists(){
        VoteType voteTypeToDelete = new VoteType();
        voteTypeToDelete.setName("NewName");
        voteTypeToDelete.setPoints(10);


        voteTypeRepository.save(voteTypeToDelete);

        voteTypeService.deleteVoteType(voteTypeToDelete.getId());
        List<VoteType> voteTypesAfter = voteTypeRepository.findAll();
        assertThat(voteTypesAfter).doesNotContain(voteTypeToDelete);
    }

    @Test
    void testDeleteVoteTypeWhenNotExists(){
        assertThrows(IllegalStateException.class, () -> voteTypeService.deleteVoteType(999));
    }

    @AfterEach
    void cleanUp(){
        voteTypeRepository.deleteAll();

    }
}
