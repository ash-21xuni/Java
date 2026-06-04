public class KnowledgeBase {
    private String term;
    private String sentence;
    private double confidenceScore;

    public KnowledgeBase(String term, String sentence, double confidenceScore){
        this.term = term;
        this.sentence = sentence;
        this.confidenceScore = confidenceScore;
   }

   public String getTerm(){
       return term;
   }

   public String getStatement(){
       return sentence;
   }

   public double getConfidenceScore(){
       return confidenceScore;
   }

   public void updateStatement(String sentence, double newConfidenceScore){
       if (newConfidenceScore>confidenceScore){
           this.sentence = sentence;
           this.confidenceScore = newConfidenceScore;
       }
   }

   @Override
   public String toString(){
       return term + " " + sentence + " " + confidenceScore;
   }

}
