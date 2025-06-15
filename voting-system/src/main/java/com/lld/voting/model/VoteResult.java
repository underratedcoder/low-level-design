package com.lld.voting.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VoteResult {
    private String candidateId;
    private String name;
    private int totalVotes;
}
