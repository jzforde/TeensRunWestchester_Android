package com.teensrunwestchester.jillianforde.teensrunwestchester;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
/**
 * Created by jillianforde on 11/28/15.
 */
public class ValidEmailTest {
    @Test
    public void validateEmail(){
        EmailFormatValidator testCase = new EmailFormatValidator();
/*Tests for missing key values. etc: @, .com or null
 */
        //test case 1: email doesn't have an @ sign. Expect to return false
        String emailWithoutAtSign = "abcde";
        boolean result1 = testCase.validate(emailWithoutAtSign);
        assertEquals(false, result1);

        //test case 30: email field is blank. Expect to return false
        String emptyEmail = "";
        boolean result30 = testCase.validate(emptyEmail);
        assertEquals(false, result30);

        //test case 31: email doesn't have any text after @ sign. Expect to return false
        String emptyAfterAt = "abc@";
        boolean result31 = testCase.validate(emptyAfterAt);
        assertEquals(false, result31);

/*Tests for invalid spaces.
 */
        //test case 2: email has spaces before text. Expect to return false
        String emailWithSpaces = "   abc@gmail.com";
        boolean result2 = testCase.validate(emailWithSpaces);
        assertEquals(false,result2);

        //test case 3: email has spaces in text before @ sign. Expect to return false.
        String emailWithSpacesBeforeAt = "a b c@gmail.com";
        boolean result3 = testCase.validate(emailWithSpacesBeforeAt);
        assertEquals(false,result3);

        //test case 4: email has spaces in text after @ sign. Expect to return false.
        String emailWithSpacesAfterAt = "abc@ gmail.com";
        boolean result4 = testCase.validate(emailWithSpacesAfterAt);
        assertEquals(false,result4);

/*Tests for valid characters.
 */
        //test case 5: email is in a valid format and has underscore before @. Expect to return true.
        String emailWithValidUnderscore = "abc_@gmail.com";
        boolean result5 = testCase.validate(emailWithValidUnderscore);
        assertEquals(true,result5);

        //test case 6: email is in a valid format and has numbers. Expect to return true.
        String emailWithValidNumbers = "abc566899088877666666655443334578754435677888888888888888765444433345@gmail.com";
        boolean result6 = testCase.validate(emailWithValidNumbers);
        assertEquals(true,result6);

        //test case 7: email is mixed capitalization. Expect to return true.
        String emailWithMixedCap = "AAbb@gmail.com";
        boolean result7 = testCase.validate(emailWithMixedCap);
        assertEquals(true,result7);

/*Tests for invalid symbols in email. Emails can only have letters, numbers and periods
 */
        //test case 8: email has more than one @. Expect to return false.
        String emailWithMultipleAt = "AAb@b@gmail.com";
        boolean result8 = testCase.validate(emailWithMultipleAt);
        assertEquals(false,result8);

        //test case 9: email has / . Expect to return false.
        String emailWithSlash = "AAb/b@gmail.com";
        boolean result9 = testCase.validate(emailWithSlash);
        assertEquals(false,result9);

        //test case 10: email has * . Expect to return false.
        String emailWithAesterisk = "AAb*b@gmail.com";
        boolean result10 = testCase.validate(emailWithAesterisk);
        assertEquals(false,result10);

        //test case 11: email has ! . Expect to return false.
        String emailWithExclamation = "AAb!b@gmail.com";
        boolean result11 = testCase.validate(emailWithExclamation);
        assertEquals(false,result11);

        //test case 12: email has ^ . Expect to return false.
        String emailWithCarrot = "AAb^b@gmail.com";
        boolean result12 = testCase.validate(emailWithCarrot);
        assertEquals(false,result12);

        //test case 13: email has % . Expect to return false.
        String emailWithPercent = "AAb%b@gmail.com";
        boolean result13 = testCase.validate(emailWithPercent);
        assertEquals(false,result13);

        //test case 14: email has $ . Expect to return false.
        String emailWithDollar = "AAb$b@gmail.com";
        boolean result14 = testCase.validate(emailWithDollar);
        assertEquals(false,result14);

        //test case 15: email has ( . Expect to return false.
        String emailWithOpenParen = "AAb(b@gmail.com";
        boolean result15 = testCase.validate(emailWithOpenParen);
        assertEquals(false,result15);

        //test case 16: email has ) . Expect to return false.
        String emailWithClosedParen = "AA)b@gmail.com";
        boolean result16 = testCase.validate(emailWithClosedParen);
        assertEquals(false,result16);

        //test case 17: email has - . Expect to return false.
        String emailWithHyphen = "AA-b@gmail.com";
        boolean result17 = testCase.validate(emailWithHyphen);
        assertEquals(false,result17);

        //test case 18: email has + . Expect to return false.
        String emailWithPlus = "AA+b@gmail.com";
        boolean result18 = testCase.validate(emailWithPlus);
        assertEquals(false,result18);

        //test case 19: email has = . Expect to return false.
        String emailWithEquals = "AA=b@gmail.com";
        boolean result19 = testCase.validate(emailWithEquals);
        assertEquals(false,result19);

        //test case 20: email has {} . Expect to return false.
        String emailWithBraces = "AA{}b@gmail.com";
        boolean result20 = testCase.validate(emailWithBraces);
        assertEquals(false,result20);

        //test case 21: email has | . Expect to return false.
        String emailWithPipe = "AA|b@gmail.com";
        boolean result21 = testCase.validate(emailWithPipe);
        assertEquals(false,result21);

        //test case 22: email has <> . Expect to return false.
        String emailWithInequality = "AA<>b@gmail.com";
        boolean result22 = testCase.validate(emailWithInequality);
        assertEquals(false,result22);

        //test case 23: email has ? . Expect to return false.
        String emailWithQuestion = "AA?b@gmail.com";
        boolean result23 = testCase.validate(emailWithQuestion);
        assertEquals(false,result23);

        //test case 24: email has ~` . Expect to return false.
        String emailWithOtherSymbols = "AA~`b@gmail.com";
        boolean result24 = testCase.validate(emailWithOtherSymbols);
        assertEquals(false,result24);

        //test case 25: email has \\ . Expect to return false.
        String emailWithBackSlash = "AA\\b@gmail.com";
        boolean result25 = testCase.validate(emailWithBackSlash);
        assertEquals(false,result25);
    }
}
