package com.fungisearch.fudriver.reclassification.estimation.command.model;

import com.fungisearch.fudriver.cycle.command.model.Cycle;
import com.fungisearch.fudriver.reclassification.command.model.RodzajSkupGrupa;
import com.fungisearch.fudriver.reclassification.estimation.command.repository.HarvestEstimationRepository;
import com.fungisearch.fudriver.user.command.model.User;
import com.fungisearch.fudriver.validation.BeanValidator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "harverst_estimation")
public class HarvestEstimation {

    transient HarvestEstimationRepository harvestEstimationRepository;
    transient BeanValidator beanValidator;

    @Id
    @GeneratedValue
    public Long id;

    @ManyToOne
    @JoinColumn(name = "cycle_id")
    private Cycle cycle;

    @Column(name = "day_no")
    private int dayNo;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private RodzajSkupGrupa rodzajSkupGrupa;

    @Column(name = "calculated_amount")
    private long calculatedAmount;

    @Column(name = "estimated_amount")
    private Long estimatedAmount;

    @ManyToOne
    @JoinColumn(name ="user_id")
    private User user;

    @Column(name = "estimation_date")
    @Temporal(TemporalType.DATE)
    private Date estimationDate;


}
