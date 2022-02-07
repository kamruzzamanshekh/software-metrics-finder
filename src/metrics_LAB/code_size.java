package metrics_LAB;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class code_size   {

	public static String fileName;
	public static long totalchar = 0;
	public static long NCLOCsum = 0, LOCsum = 0, commentLinesum = 0,
			blankLinesum = 0, Dlinesum = 0, totalcharsum = 0,totalbytessum=0,methodsum=0,classsum=0,
			totalHalsteadprogramValume=0,interfacesum=0,complexitysum=0;
	public static String projectName = "E:\\3-2 semester\\software metrics LAB\\Airplane-sky-force";
	//public static String projectName="D:\\Downlaods\\downloaded Files\\Wizard-Chess-master";
	public static File csvfile=new File("C:\\Users\\ShEkH\\Desktop\\metrics txt file\\ReportTable.csv");
	public static Set<String> packages = new HashSet<String>();
	public static Set<String> keywordList=new HashSet<String>();
	public static Set<String> operatorSet=new HashSet<String>();
	public static int totalOperatorOccerance=0;
	public static HashMap<String, Integer> uniqueOperatorList = new HashMap<String, Integer>();
	public static HashMap<String,Integer> uniquesOperandList=new HashMap<String,Integer>();
	
	public static void main(String[] args) throws IOException {
		PrintWriter csvWriter=new PrintWriter(csvfile);
		csvWriter.print("Class Name , NCLOC , CLC , BLC ,LOC , DD , TotalBytes"
				+ ", Methods , Cyclomatic_complexity , Halstead's Volume\n");
		List<File> list=listf(projectName);
		for(File file: list) {
			if(file.getAbsolutePath().endsWith(".java")) {
				fileName = file.getAbsolutePath();
				int n1,n2,N1=0,N2=0;
				String[] keywords = { "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class",
						"continue", "default", "do", "double", "else", "enum", "extends", "final", "finally", "float", "float",
						"for", "if", "implements", "import", "instanceof", "int", "interface", "long", "native", "new", "null",
						"package", "private", "protected", "public", "return", "static", "striptfc", "super", "switch",
						"synchronized", "this", "throw", "throws", "transient", "try", "void", "volatile", "while", "const",
						"goto","goto","import","+", "-", "*", "/", "++", "--", "%", "==", "!=", "=", "<", ">", "<=", ">=", "&", "|",
						"^", "~", ">>", "<<", ">>>", "&&", "||", "!", "=", "+=", "-=", "*=", "/=", "%=", "<<=", ">>=", "&=",
						"^=", "|=","(",")","{","}","[","]",".",",",";",":","\"","'"};
				keywordList.addAll(Arrays.asList(keywords));
				System.out.println("=======================================================");
				System.out.println("Metrices for the " + fileName);
				System.out.println("=======================================================");
				
				//System.out.println(fileName);
				long NCLOC = 0;
				long LOC = 0;
				long commentLine = 0;
				long blankLine = 0;
				long Dline = 0;
				long totalchar = 0;
				String line = null;
				int methods=0;
				int classes=0;
				int interfaces=0;
				int Cyclomatics=0;
				int classcyclomatics=0;
				int HalsteadprogramValume=0;

				// Checking
				try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
					while ((line = reader.readLine()) != null) {
						operatorsCheck(line,keywords);
						operandsCheck(line,keywords);
						totalchar += line.length();
						if (isBlank(line) || isComment(line)) {
							if (isComment(line)) {
								commentLine++;
								// System.out.println("c line: "+line);
							} else {
								blankLine++;
								// System.out.println("b: "+blankLine);
							}
						} else {
							NCLOC++;
							if (isDataDeclaration(line)) {

								Dline++;
							}
                              if(isClass(line)) {
                            	  classes++;
								
							}
							if(isMethod(line)) {
								methods++;
							} 
							if(isInterface(line)) {
								interfaces++;
							}
							if(isPackage(line)) {
								String packageLine=line.replaceAll(" ", "");
								packages.add(packageLine);
							}
							//cyclomatics complexity
							Cyclomatics=complexity(line);
							complexitysum+=Cyclomatics;
							classcyclomatics+=Cyclomatics;
							
							
						}

					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				n1=uniqueOperatorList.size();
				n2=uniquesOperandList.size();
				for(int i:uniqueOperatorList.values()) {
					N1+=i;
				}
				for(int i:uniquesOperandList.values()) {
					N2+=i;
				}
				LOC = (NCLOC + commentLine + blankLine);
				HalsteadprogramValume=((N1+N2) * (int)(Math.log(n1+n2)/Math.log(2)));
				
				//Display Output
				System.out.println("LOC: " + LOC);
				System.out.println("Commente Line: " + commentLine);
				System.out.println("Blank Line: " + blankLine);
				System.out.println("Data Declaration: " + Dline);
				System.out.println("NCLOC: " + NCLOC);
				System.out.println("Density of comments: "
						+ (Double.longBitsToDouble(commentLine) / Double.longBitsToDouble(LOC)) * 100);

				Path path = Paths.get(fileName);
				System.out.println("Number of bytes: " + Files.size(path));
				System.out.println("Total CHAR: " + totalchar);
				System.out.println("Number of methods "+methods);
				System.out.println("Class Cyclomatics complexity: "+classcyclomatics);
				System.out.println("Halstead;s Program Volume : "+HalsteadprogramValume);
				
				//export CSV file from java
				
				
				csvWriter.print(file.getName()+" ,"+ NCLOC+" ,"+commentLine+" ,"+ blankLine+" ," + LOC+" ," + Dline+" ," + Files.size(path)+" ,"+ methods+ " ," 
				+classcyclomatics+" ,"+ HalsteadprogramValume+"\n");

				
				
				
				
				// For merging all file metrics
				//calculate number o methods
				

				NCLOCsum += NCLOC;
				LOCsum += LOC;
				commentLinesum += commentLine;
				blankLinesum += blankLine;
				Dlinesum += Dline;
				totalcharsum += totalchar;
				totalbytessum+=Files.size(path);
				methodsum+=methods;
				classsum+=classes;
				interfacesum+=interfaces;
				totalHalsteadprogramValume+=HalsteadprogramValume;
				
				
				

			}

		}
		System.out.println("                                        =================================");
		System.out.println("                                        Total project Code Size metrices: ");
		System.out.println("                                        =================================");

		System.out.println("Total LOC: " + LOCsum);
		System.out.println("Tota Commente Line: " + commentLinesum);
		System.out.println("Total Blank Line: " + blankLinesum);
		System.out.println("Total Data Declaration: " + Dlinesum);
		System.out.println("Total NCLOC: " + NCLOCsum);
		System.out.println("Total Density of comments: "
				+ (Double.longBitsToDouble(commentLinesum) / Double.longBitsToDouble(LOCsum)) * 100);
		System.out.println("Number of bytes: " + totalbytessum);
		System.out.println("Total CHAR: " + totalcharsum);
		System.out.println("Total methods: " + methodsum);
		System.out.println("Total classes : " + classsum);
		System.out.println("Total interfaces : " + interfacesum);
		System.out.println("Weighted or methods avarage per class: "+(methodsum/(classsum+interfacesum)));
		System.out.println("Total Number of packages: "+packages.size());
		System.out.println("Project Cyclomatics complexity: "+complexitysum);
		System.out.println("Total Halstead's Program Valume: "+totalHalsteadprogramValume);
		System.out.println("Average Halstead's Program Valume per class : "+totalHalsteadprogramValume/classsum);
        csvWriter.close();
	}

	private static int complexity(String line) {
		// TODO Auto-generated method stub
		int complexity=0;
		 String[] buffer=line.split(" ");
		 for(String word: buffer) {
			 if(word.contains("if")||word.contains("else")||word.contains("case")||word.contains("for")||word.contains("while")
				 ||word.contains("foreach")||word.contains("default")) {
				 complexity++;
				 
			 }
		 }
		return complexity;
	}

	private static boolean isPackage(String line) {
		
		 Pattern pattern = Pattern.compile("package ([\\w&&\\D]([\\w\\.]*[\\w])?);");
		 Matcher matcher = pattern.matcher(line);
			return matcher.find();
	}

	private static boolean isInterface(String line) {
		// TODO Auto-generated method stub
		 Pattern pattern = Pattern.compile("(((|public|final|abstract|private|static|protected)(\\s+))?interface(\\s+)(\\w+)(<.*>)?(\\s+extends\\s+\\w+)?(<.*>)?(\\s+implements\\s+)?(.*)?(<.*>)?(\\s*))\\{$");
		 Matcher matcher = pattern.matcher(line);
			return matcher.find();
	}

	private static boolean isClass(String line) {
		// TODO Auto-generated method stub
		 Pattern pattern = Pattern.compile("(((|public|final|abstract|private|static|protected)(\\s+))?(class)(\\s+)(\\w+)(<.*>)?(\\s+extends\\s+\\w+)?(<.*>)?(\\s+implements\\s+)?(.*)?(<.*>)?(\\s*))\\{$");
		 Matcher matcher = pattern.matcher(line);
			return matcher.find();
	}

	private static boolean isMethod(String line) {
		// TODO Auto-generated method stub
		Pattern pattern = Pattern.compile("(public|protected|private|static|\\s) +[\\w\\<\\>\\[\\],\\s]+\\s+(\\w+) *\\([^\\)]*\\) *(\\{?|[^;])");
		Matcher matcher = pattern.matcher(line);

		return matcher.find();
	}

	private static boolean isDataDeclaration(String line) {
		long Dline = 0;
		String[] datalist = null;
		// datalist=line.split(" ");
		// for(String chr : datalist) {
		if (line.contains("int") || line.contains("boolean") || line.contains("byte") || line.contains("char")
				|| line.contains("short") || line.contains("long") || line.contains("float") || line.contains("double")
				|| line.contains("String")) {

			return true;
			// }
		}
		return false;
	}

	private static boolean isComment(String line) {
		// TODO Auto-generated method stub
		// Pattern
		// pattern=Pattern.compile("//.*|(\"(?:\\\\[^\"]|\\\\\"|.)*?\")|(?s)/\\*.*?\\*/");
		Pattern pattern = Pattern.compile("(/\\*([^*]|[\\r\\n]|(\\*+([^*/]|[\\r\\n])))*\\*+/)|(//.*)");
		Matcher matcher = pattern.matcher(line);

		return matcher.find();
	}

	private static boolean isBlank(String line) {
		// TODO Auto-generated method stub
		if (line.isBlank()) {
			return true;
		}
		return false;
	}
	public static List<File> listf(String directoryName) {
        File directory = new File(directoryName);

        List<File> resultList = new ArrayList<File>();

        // get all the files from a directory
        File[] fList = directory.listFiles();
        resultList.addAll(Arrays.asList(fList));
        for (File file : fList) {
            if (file.isFile()) {
               // System.out.println(file.getAbsolutePath());
            } else if (file.isDirectory()) {
                resultList.addAll(listf(file.getAbsolutePath()));
            }
        }
        //System.out.println(fList);
        return resultList;
    } 
	public static String[] report(String line) {
		
		return null;
		
	}
	private static void operandsCheck(String line, String[] keywords) {

		String[] words = line.split(" |(<<)|(\\|\\|)|(synchronized)|(<=)|(do)|(float)|(while)|(protected)|(continue)|"
				+ "(else)|(catch)|(if)|(case)|(^=)|(--)|(==)|(new)|(!)|(\")|(package)|(static)|(void)|(%)|"
				+ "(byte)|(double)|(&)|(finally)|(')|(this)|(\\()|(\\))|(\\*)|(\\+)|(throws)|(,)|(-)|(enum)|"
				+ "(\\.)|(/)|(-=)|(extends)|(null)|(transient)|(final)|(%=)|(try)|(:)|(;)|(!=)|(<)|(=)|"
				+ "(<<=)|(>)|(>=)|(implements)|(>>)|(&&)|(\\|=)|(private)|(import)|(const)|(for)|(interface)|"
				+ "(long)|(switch)|(default)|(goto)|(\\*=)|(public)|(native)|(assert)|(&=)|(\\[)|(class)|"
				+ "(\\])|(\\^)|(\\++)|(break)|(striptfc)|(volatile)|(abstract)|(int)|(/=)|(instanceof)|"
				+ "(super)|(\\+=)|(boolean)|(throw)|(char)|(\\{)|(\\|)|(>>=)|(\\})|(return)|(~)|(>>>)|(String)");

		for (String operand : words) {

			if (!operand.isBlank()) {

				int count = uniquesOperandList.containsKey(operand) ? uniquesOperandList.get(operand) : 0;
				uniquesOperandList.put(operand.trim(), count + 1);
			}

		}

	}


	private static void operatorsCheck(String line, String[] keywords) {
		for (String key : keywords) {
			if (line.contains(key)) {
				// totalOperatorOccerance++;
				int count = uniqueOperatorList.containsKey(key) ? uniqueOperatorList.get(key) : 0;
				uniqueOperatorList.put(key, count + 1);
			}
		}

	}
}
