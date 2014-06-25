package md.varoinform.model.search;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class QueryAnalyzerTest {

    @Test
    public void testGetTokensMail() throws Exception {
        String q1 = "varo-inform@varo-inform.com";
        Object[] expecteds = {q1};
        analyze(q1, expecteds);
    }

    public void analyze(String q1, Object[] expecteds) {
        QueryAnalyzer analyzer = new QueryAnalyzer(q1);
        List<String> tokens = analyzer.getTokens();
        System.out.println(tokens);
        Arrays.sort(expecteds);
        Object[] actuals = tokens.toArray();
        Arrays.sort(actuals);
        assertArrayEquals(expecteds, actuals);
    }

    @Test
    public void testTitle() throws Exception {
        String q1 = "varo-inform";
        Object[] expected = {"varo", "inform"};
        analyze(q1, expected);
    }

    @Test
    public void testTitleMail() throws Exception {
        String q1 = "varo-inform varo-inform@varo-inform.com";
        Object[] expected = {"varo", "inform", "varo-inform@varo-inform.com"};
        analyze(q1, expected);
    }

    @Test
    public void testURL() throws Exception {
        System.out.println("test url");
        String q1 = "varo-inform.com";
        Object[] expected = {"varo", "inform", "com"};
        analyze(q1, expected);

        q1 = "www.zumba-fitness.md";
        expected = new Object[]{"www", "zumb", "fit"};
        analyze(q1, expected);
        System.out.println("end test url");
    }

    @Test
    public void testSoondookCom() throws Exception {
        String q1 = "soondook.com";
        Object[] expected = {"soondook", "com"};
        analyze(q1, expected);
    }
}