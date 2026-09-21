package Problem6;

import org.apache.hadoop.hive.ql.exec.UDF;

public class SpendBucketUDF extends UDF {

    public String evaluate(double amount) {

        if (amount < 1000) {

            return "Low";

        } else if (amount < 5000) {

            return "Medium";

        } else {

            return "High";
        }
    }
}