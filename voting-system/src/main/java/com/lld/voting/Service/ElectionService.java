package com.lld.voting.Service;

import com.lld.voting.model.VoteResult;
import com.voting.model.Candidate;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ElectionService {

    private final Map<String, Candidate> candidates = new ConcurrentHashMap<>();
    private final Map<String, AtomicInteger> voteCounts = new ConcurrentHashMap<>();
    private final Set<String> votedVoters = ConcurrentHashMap.newKeySet();
    private final Set<String> candidateIds = ConcurrentHashMap.newKeySet();

    public void registerCandidate(String candidateId, String name) {
        if (candidates.containsKey(candidateId)) {
            throw new IllegalArgumentException("Candidate already registered: " + candidateId);
        }
        candidates.put(candidateId, new Candidate(candidateId, name));
        voteCounts.put(candidateId, new AtomicInteger(0));
        candidateIds.add(candidateId);
    }

    public boolean castVote(String voterId, String candidateId) {
        // Cannot vote twice
        if (votedVoters.contains(voterId)) return false;

        // Candidates cannot vote
        if (candidateIds.contains(voterId)) return false;

        // Candidate must be valid
        if (!candidates.containsKey(candidateId)) return false;

        // Register the vote
        votedVoters.add(voterId);
        voteCounts.get(candidateId).incrementAndGet();
        return true;
    }

    public VoteResult getWinner() {
        String winnerId = null;
        int maxVotes = -1;

        for (Map.Entry<String, AtomicInteger> entry : voteCounts.entrySet()) {
            int votes = entry.getValue().get();
            if (votes > maxVotes) {
                maxVotes = votes;
                winnerId = entry.getKey();
            }
        }

        if (winnerId == null) {
            return null;
        }

        Candidate winner = candidates.get(winnerId);
        return new VoteResult(winner.getCandidateId(), winner.getName(), maxVotes);
    }

    public Map<String, Integer> getAllVoteCounts() {
        Map<String, Integer> result = new HashMap<>();
        voteCounts.forEach((key, value) -> result.put(key, value.get()));
        return result;
    }
}
