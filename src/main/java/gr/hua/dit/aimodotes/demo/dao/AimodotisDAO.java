package gr.hua.dit.aimodotes.demo.dao;

import gr.hua.dit.aimodotes.demo.entity.Aimodotis;

import java.util.List;

public interface AimodotisDAO {
    public List<Aimodotis> getAimodotes();
    public Aimodotis getAimodotis(Integer aimodotis_id);
    public Aimodotis saveAimodotis(Aimodotis aimodotis);
    public void deleteAimodotis(Integer aimodotis_id);
}
