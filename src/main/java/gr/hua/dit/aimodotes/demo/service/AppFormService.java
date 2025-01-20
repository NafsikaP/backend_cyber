package gr.hua.dit.aimodotes.demo.service;

import gr.hua.dit.aimodotes.demo.dao.AimodotisDAO;
import gr.hua.dit.aimodotes.demo.entity.Aimodotis;
import gr.hua.dit.aimodotes.demo.entity.AppForm;
import gr.hua.dit.aimodotes.demo.repository.AppFormRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppFormService {
    @Autowired
    private AimodotisDAO aimodotisDAO;

    @Autowired
    private AppFormRepository appFormRepository;

    //save app form
    @Transactional
    public void saveAppForm(AppForm appForm,Integer aimodotis_id){
        Aimodotis aimodotis = aimodotisDAO.getAimodotis(aimodotis_id);
        appForm.setAimodotis(aimodotis);
        appFormRepository.save(appForm);
    }

    //delete app form
    @Transactional
    public void deleteAppForm(Integer appform_id){
        AppForm appForm = appFormRepository.findById(appform_id)
                .orElseThrow(()->new EntityNotFoundException("Application Form not found with id: "+ appform_id));
        appFormRepository.delete(appForm);
    }

    //get all app forms
    @Transactional
    public List<AppForm> getAppForms(){
        return appFormRepository.findAll();
    }

    //get one app form
    @Transactional
    public AppForm getAppForm(Integer appform_id){
        return appFormRepository.findById(appform_id).get();
    }


}
