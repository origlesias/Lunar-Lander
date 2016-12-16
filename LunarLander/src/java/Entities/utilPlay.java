

package Entities;

// @author: Oriol Iglesias

public class utilPlay {
    private Integer id;
    private String startDate;
    private String finishDate;
    private int score;
    private String userId;

    public utilPlay(Integer id, String startDate, String finishDate, int score, Users userId) {
        this.id = id;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.score = score;
        this.userId = userId.getNick();
    }
    
    
}
