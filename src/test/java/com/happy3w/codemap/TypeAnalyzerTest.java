package com.happy3w.codemap;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class TypeAnalyzerTest {

    @Test
    public void should_analyze_simple() {
        List<String> types = TypeAnalyzer.analyzeTypes("Lcom.happy3w.codemap.TypeAnalyzer");
        Assert.assertEquals("[com.happy3w.codemap.TypeAnalyzer]", types.toString());
    }

    @Test
    public void should_analyze_multiple() {
        List<String> types = TypeAnalyzer.analyzeTypes("Ljava/util/List<Lcom/happy3w/lotterycalculator/LotteryCalculator$NumWeight;>;");
        Assert.assertEquals("[java.util.List, com.happy3w.lotterycalculator.LotteryCalculator$NumWeight]", types.toString());
    }
}
