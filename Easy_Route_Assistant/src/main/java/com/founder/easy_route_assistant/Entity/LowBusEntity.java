package com.founder.easy_route_assistant.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "bus")
public class LowBusEntity {
    @Id
    String busnum;
    String routeid;
}
