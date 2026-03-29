package com.sahil.votethegoat;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

import com.sahil.votethegoat.model.Vote;
import com.sahil.votethegoat.repository.VoteRepository;
import com.sahil.votethegoat.service.VoteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class VotingFlowTest {

    @Mock
    private VoteRepository voteRepository;

    @InjectMocks
    private VoteService voteService;

    @Test
    public void testCastVote_Success() throws Exception {
        Vote vote = new Vote();
        vote.setElectionId("elec1");
        vote.setVoterId("user1");
        vote.setCandidateId("cand1");

        when(voteRepository.findByElectionIdAndVoterId("elec1", "user1")).thenReturn(Optional.empty());
        when(voteRepository.save(any(Vote.class))).thenReturn(vote);

        Vote savedVote = voteService.castVote(vote);
        assertNotNull(savedVote);
        assertEquals("user1", savedVote.getVoterId());
    }

    @Test
    public void testCastVote_Duplicate_Failure() {
        Vote vote = new Vote();
        vote.setElectionId("elec1");
        vote.setVoterId("user1");

        when(voteRepository.findByElectionIdAndVoterId("elec1", "user1")).thenReturn(Optional.of(new Vote()));

        Exception exception = assertThrows(Exception.class, () -> {
            voteService.castVote(vote);
        });

        assertEquals("You have already voted in this election!", exception.getMessage());
    }
}
