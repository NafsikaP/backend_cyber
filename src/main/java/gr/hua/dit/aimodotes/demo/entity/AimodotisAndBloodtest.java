package gr.hua.dit.aimodotes.demo.entity;

public class AimodotisAndBloodtest {
    //wrapper class
    //this class is used to combine blood donor and his blood test
    //it is used for the method on the appform rest controller to submit a new appform
    //so that on the request body on the parameter we can take the body and seperate it on the different entities
    private Aimodotis aimodotis;
    private BloodTest bloodTest;

    public Aimodotis getAimodotis() {
        return aimodotis;
    }

    public void setAimodotis(Aimodotis aimodotis) {
        this.aimodotis = aimodotis;
    }

    public BloodTest getBloodTest() {
        return bloodTest;
    }

    public void setBloodTest(BloodTest bloodTest) {
        this.bloodTest = bloodTest;
    }

    public AimodotisAndBloodtest(Aimodotis aimodotis, BloodTest bloodTest) {
        this.aimodotis = aimodotis;
        this.bloodTest = bloodTest;
    }

    public AimodotisAndBloodtest() {
    }
}
