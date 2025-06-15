package com.lld.voting;

import com.lld.voting.Service.ElectionService;
import com.lld.voting.model.VoteResult;

public class VotingApp {
    public static void main(String[] args) {
        ElectionService electionService = new ElectionService();

        // Register Candidates
        electionService.registerCandidate("c1", "Alice");
        electionService.registerCandidate("c2", "Bob");
        electionService.registerCandidate("c3", "Charlie");

        // Voting
        electionService.castVote("v1", "c1");
        electionService.castVote("v2", "c1");
        electionService.castVote("v3", "c2");
        electionService.castVote("v4", "c1");
        electionService.castVote("c1", "c2"); // Candidate trying to vote — ignored
        electionService.castVote("v1", "c2"); // Duplicate voter — ignored

        // Get Winner
        VoteResult winner = electionService.getWinner();
        System.out.println("Election Winner: " + winner);
    }
}
