package org.geometerplus.fbreader.tree;

import android.annotation.TargetApi;
import android.os.Build;
import java.text.Normalizer;

public class MyNormalizer {
    public final static String[] articles;

    static {
        articles = new String[]{
                //English
                "the ", "a ", "an ",
                //French
                "un ", "une ", "le ", "la ", "les ", "du ", "de ", "des ", "l ", "d ",
                //Deutsch
                "das ", "des ", "dem ", "die ", "der ", "den ", "ein ", "eine ", "einer ", "einem ", "einen ", "eines "
        };
    }

	public static String normalize(String myTitle) {
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			myTitle = checkNorm(myTitle);
		}
		StringBuilder s = new StringBuilder();
		char ch;
		for (int i = 0; i < myTitle.length(); i++) {
			ch = myTitle.charAt(i);
			//In case it is d' or l', may be it is "I'm", but it's OK.
			if ('\'' == ch) {
				ch = ' ';
			}
			//If it looks like a space, it's a space
            if (Character.isWhitespace(ch) && ' ' != ch) {
                ch = ' ';
            } else if ('\u00A0' == ch || '\u2007' == ch || '\u202F' == ch) {
                ch = ' ';
            }
			switch (Character.getType(ch))	{
				case Character.UPPERCASE_LETTER:
				case Character.TITLECASE_LETTER:
					ch = Character.toLowerCase(ch);	
				case Character.LETTER_NUMBER:
				case Character.OTHER_LETTER:
				case Character.MODIFIER_LETTER:
				case Character.LOWERCASE_LETTER:
				case Character.DECIMAL_DIGIT_NUMBER:
					s.append(ch);
					break;
				case Character.DIRECTIONALITY_WHITESPACE:
					// if s is not empty, it's not a last symbol and there is no whitespace _before_ it
					if (0 != s.length() && (myTitle.length() - 1 != i) && (' ' != s.charAt(s.length() - 1))) {
						s.append(ch);
					}
					break;
					}
			}
		s = deleteArticles(s);
		return s.toString();
	}
	
	public static StringBuilder deleteArticles(StringBuilder s) {
		for (String i: articles) {
			if (0 == s.indexOf(i)) {
				s.delete(0, i.length());
			}
		}
		return s;
	}
	
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public static String checkNorm(String s) {
		if (!Normalizer.isNormalized(s, Normalizer.Form.NFKD)) {
    		s = Normalizer.normalize(s, Normalizer.Form.NFKD);
    		}
		return s;
	}
	
    /*public static StringBuilder deleteWhitespaces(StringBuilder s) {
        while (-1 != s.indexOf(" ")) {
            s.deleteCharAt(s.indexOf(" "));
        }
        return s;
    }*/
}