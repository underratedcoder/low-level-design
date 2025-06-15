package com.lld.filesystem.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserSession {
    private User user;
    private Directory currentDirectory;
}