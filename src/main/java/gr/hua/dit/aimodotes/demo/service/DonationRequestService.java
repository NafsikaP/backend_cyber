package gr.hua.dit.aimodotes.demo.service;

import gr.hua.dit.aimodotes.demo.dao.AimodotisDAO;
import gr.hua.dit.aimodotes.demo.entity.Aimodotis;
import gr.hua.dit.aimodotes.demo.entity.DonationRequest;
import gr.hua.dit.aimodotes.demo.repository.DonationRequestRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DonationRequestService {
    @Autowired
    private AimodotisDAO aimodotisDAO;

    @Autowired
    private DonationRequestRepository donationRequestRepository;

    //get all donation requests
    @Transactional
    public List<DonationRequest> getDonationRequests(){
        return donationRequestRepository.findAll();
    }

    //save a donation request
    @Transactional
    public void saveDonationRequest(DonationRequest donationRequest){
        donationRequestRepository.save(donationRequest);
    }

    //delete a donation request
    @Transactional
    public void deleteDonationRequest(Integer donationrequest_id){
        donationRequestRepository.deleteById(donationrequest_id);
    }

    //get a donation request
    @Transactional
    public DonationRequest getDonationRequest(Integer donationrequest_id){
        return donationRequestRepository.findById(donationrequest_id).get();
    }

    //get a donation request linked to a blood donor
    public List<DonationRequest> getAimodotisDonationBloodRequests(Integer aimodotis_id){
        Aimodotis aimodotis = aimodotisDAO.getAimodotis(aimodotis_id);
        return aimodotis.getDonationRequests();
    }
}
