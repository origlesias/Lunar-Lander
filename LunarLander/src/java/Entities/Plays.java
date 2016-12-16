

package Entities;

// @author: Oriol Iglesias

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;



@Entity
@Table(name = "plays")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Plays.findAll", query = "SELECT p FROM Plays p"),
    @NamedQuery(name = "Plays.findById", query = "SELECT p FROM Plays p WHERE p.id = :id"),
    @NamedQuery(name = "Plays.findByStartDate", query = "SELECT p FROM Plays p WHERE p.startDate = :startDate"),
    @NamedQuery(name = "Plays.findByFinishDate", query = "SELECT p FROM Plays p WHERE p.finishDate = :finishDate"),
    @NamedQuery(name = "Plays.findByScore", query = "SELECT p FROM Plays p WHERE p.score = :score")})
public class Plays implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "start_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Basic(optional = false)
    @Column(name = "finish_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date finishDate;
    @Basic(optional = false)
    @Column(name = "score")
    private int score;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    private Users userId;

    public Plays() {
    }

    public Plays(Integer id) {
        this.id = id;
    }

    public Plays(Integer id, Date startDate, Date finishDate, int score) {
        this.id = id;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.score = score;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Users getUserId() {
        return userId;
    }

    public void setUserId(Users userId) {
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Plays)) {
            return false;
        }
        Plays other = (Plays) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Plays[ id=" + id + " ]";
    }

}
