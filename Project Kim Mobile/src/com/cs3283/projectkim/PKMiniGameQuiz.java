package com.cs3283.projectkim;

import java.util.Random;

public class PKMiniGameQuiz {
	// 12 questions
	public static final String[] questionArray = {	"TAIPEI 101 IS THE TALLEST BUILDING IN THE WORLD?", //NO
					                                "NUS WAS FOUNDED AT 1963.", //NO
					                                "NUS HAS 3 CAMPUS LOCATION.", //YES
					                                "CDTL WAS FIRST ESTABLISHED ON 1984?", //YES
					                                "THERE ARE ABOUT 2000 RESIDENTIAL PLACES AROUND NUS CAMPUS?", //NO
					                                "NUCLEAR SIZES ARE EXPRESSED IN A UNIT NAMED FERMI?", //YES
					                                "THE HEIGHT OF EVEREST IS 8481 METRES? ", //NO
					                                "THE FASTEST MOVING LAND SNAKE IN THE WORLD IS ANACONDA?", //NO
					                                "NGULTRUM IS THE CURRENCY OF BHUTAN?", //YES
					                                "DR. JONAK SOLK DISCOVERED POLIO VACCINE?", //NO
					                                "THE RAINIEST SPOT IN THE WORLD IS CHERRAPUNJI?", //YES
					                                "SEWING MACHINE WAS INVENTED BY ELIAS HOME FROM USA?"}; //no
	public static int[] answerArray = {0,0,1,1,0,1,0,0,1,0,1,0}; // 0-false; 1-true
	
	public String getQuestion(int qnsNo){
		return questionArray[qnsNo];
	}
	
	public int getAnswer(int qnsNo){
		return answerArray[qnsNo];
	}
}
