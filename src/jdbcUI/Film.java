package jdbcUI;

/**
 * Film
 */
public class Film {

    private String titre;
    private int annee;
    private float duree;

    public Film(String titre, int annee, float duree){
        this.titre = titre;
        this.annee = annee; 
        this.duree = duree;
    }

    /**
     * @return the titre
     */
    public String getTitre() {
        return titre;
    }

    /**
     * @return the annee
     */
    public int getAnnee() {
        return annee;
    }

    /**
     * @return the duree
     */
    public float getDuree() {
        return duree;
    }

    /**
     * @param titre the titre to set
     */
    public void setTitre(String titre) {
        this.titre = titre;
    }

    /**
     * @param annee the annee to set
     */
    public void setAnnee(int annee) {
        this.annee = annee;
    }

    /**
     * @param duree the duree to set
     */
    public void setDuree(float duree) {
        this.duree = duree;
    }
}