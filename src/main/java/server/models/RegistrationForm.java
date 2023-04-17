package server.models;

import java.io.Serializable;

/**
 * Classe qui définit une fiche d'inscription.
 */
public class RegistrationForm implements Serializable {
    private String prenom;
    private String nom;
    private String email;
    private String matricule;
    private Course course;

    /**
     * Méthode qui construit une fiche d'inscription. C'est le constructeur de la classe.
     * @param prenom Prénom de l'étudiant
     * @param nom Nom de l'étudiant
     * @param email Email de l'étudiant
     * @param matricule Matricule de l'étudiant
     * @param course Le cours que l'étudiant s'inscrit à
     */
    public RegistrationForm(String prenom, String nom, String email, String matricule, Course course) {
        this.prenom = prenom;
        this.nom = nom;
        this.email = email;
        this.matricule = matricule;
        this.course = course;
    }

    /**
     * Getter pour récupérer un prénom.
     * @return le prénom
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * Setter pour assigner un prénom.
     * @param prenom le prénom
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     * Getter pour récupérer un nom.
     * @return le nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * Setter pour assigner un nom.
     * @param nom le nom
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Getter pour récupérer l'email d'un étudiant.
     * @return l'email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter pour assigner un email d'un étudiant.
     * @param email l'émail
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Getter pour récupérer une matricule d'un étudiant.
     * @return la matricule
     */
    public String getMatricule() {
        return matricule;
    }

    /**
     * Setter pour assigner une matricule d'un étudiant.
     * @param matricule la matricule
     */
    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    /**
     * Getter pour récupérer le cours d'un étudiant.
     * @return le cours
     */
    public Course getCourse() {
        return course;
    }

    /**
     * Setter pour assigner le cours d'un étudiant.
     * @param course le cours
     */
    public void setCourse(Course course) {
        this.course = course;
    }

    /**
     * Méthode qui prend l'ensemble des attribut de la classe, et les convertis en un string facilement déchiffrable.
     * @return le string
     */
    @Override
    public String toString() {
        return "InscriptionForm{" + "prenom='" + prenom + '\'' + ", nom='" + nom + '\'' + ", email='" + email + '\'' + ", matricule='" + matricule + '\'' + ", course='" + course + '\'' + '}';
    }
}
