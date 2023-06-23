package org.umbrella.tracker;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.NONE)
public class Student {
    private String firstName;
    private String lastName;
    private String email;
}
