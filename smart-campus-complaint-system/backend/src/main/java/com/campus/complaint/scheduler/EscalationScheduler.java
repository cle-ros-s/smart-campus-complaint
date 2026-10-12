package com.campus.complaint.scheduler;

import com.campus.complaint.entity.Complaint;
import com.campus.complaint.entity.Escalation;
import com.campus.complaint.enums.ComplaintStatus;
import com.campus.complaint.repository.ComplaintRepository;
import com.campus.complaint.repository.EscalationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@EnableScheduling
public class EscalationScheduler {

    private static final Logger logger = LoggerFactory.getLogger(EscalationScheduler.class);

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private EscalationRepository escalationRepository;

    // Runs every hour to check for overdue complaints
    @Scheduled(fixedRate = 3600000)
    public void checkAndEscalateOverdueComplaints() {
        logger.info("Running scheduled escalation check...");

        List<Complaint> activeComplaints = complaintRepository.findAll().stream()
                .filter(c -> c.getStatus() != ComplaintStatus.RESOLVED 
                          && c.getStatus() != ComplaintStatus.CLOSED 
                          && c.getStatus() != ComplaintStatus.ESCALATED)
                .collect(Collectors.toList());

        for (Complaint c : activeComplaints) {
            if (c.getDueAt() != null && LocalDateTime.now().isAfter(c.getDueAt())) {
                logger.warn("Complaint {} is overdue! Escalating...", c.getComplaintNumber());
                
                c.setStatus(ComplaintStatus.ESCALATED);
                complaintRepository.save(c);

                Escalation escalation = new Escalation();
                escalation.setComplaint(c);
                escalation.setReason("Automatically escalated due to SLA violation.");
                escalationRepository.save(escalation);
                
                // Here we would also trigger a Notification to Dept Head / Admin
            }
        }
    }
}
