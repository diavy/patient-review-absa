/**
 * Created by xshuai on 1/19/17.
 */

import java.util.Date;
import java.util.List;

public class Review {
    protected String id;
    protected Clinic clinic;
    protected int rating;
    protected Date date;
    protected Patient patient;


    protected List<Aspect> aspects;
    //protected Aspect asp;
    protected List<String> polaritySentences;

}
