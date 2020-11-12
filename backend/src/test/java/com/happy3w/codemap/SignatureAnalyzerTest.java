package com.happy3w.codemap;

import com.happy3w.codemap.component.SignatureAnalyzer;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

public class SignatureAnalyzerTest {

    @Test
    public void should_analyze_simple() {
        List<String> types = new SignatureAnalyzer().analyzeTypes("Lcom/happy3w/codemap/TypeAnalyzer")
                .collect(Collectors.toList());
        Assert.assertEquals("[com/happy3w/codemap/TypeAnalyzer]", types.toString());
    }

    @Test
    public void should_analyze_multiple() {
        List<String> types = new SignatureAnalyzer().analyzeTypes("Ljava/util/List<Lcom/happy3w/lotterycalculator/LotteryCalculator$NumWeight;>;")
                .collect(Collectors.toList());
        Assert.assertEquals("[com/happy3w/lotterycalculator/LotteryCalculator$NumWeight]", types.toString());
    }
}
