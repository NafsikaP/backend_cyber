package gr.hua.dit.aimodotes.demo.dao;

import gr.hua.dit.aimodotes.demo.entity.Secretary;

import java.util.List;

public interface SecretaryDAO {
    public List<Secretary> getSecretaries();
    public Secretary getSecretary(Integer secretary_id);
    public Secretary saveSecretary(Secretary secretary);
    public void deleteSecretary(Integer secretary_id);
}
