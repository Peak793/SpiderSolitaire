package sample;

public class Score implements Comparable{
    private String name ="";
    private int score = 0;

    Score ()
    {}

    Score (String name,int score)
    {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public int compareTo(Object o) {
        Score score2 = (Score)o;
        if(this.score == score2.score)
        {
            return 0;
        }else if(this.score < score2.score)
        {
            return -1;
        }else
        {
            return 1;
        }
    }
}
