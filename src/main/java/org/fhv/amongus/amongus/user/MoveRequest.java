package org.fhv.amongus.amongus.user;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoveRequest {
    private Long id;
    private String direction;
}