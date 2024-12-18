import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.Math.pow;

public class Day17 {

  public sealed interface OperandD17 {
    String print();

    long invoke(long[] registers);

    Literal[] literalCache = IntStream.range(0, 8).mapToObj(Literal::new).toArray(Literal[]::new);

    Combo[] comboCache = {new Combo.O0(), new Combo.O1(), new Combo.O2(), new Combo.O3(), new Combo.O4(), new Combo.O5(), new Combo.O6(), new Combo.O7()};

    static Combo getCombo(int value) {
      return comboCache[value];
    }

    record Literal(long value) implements OperandD17 {
      @Override
      public String print() {
        return Long.toString(value);
      }

      @Override
      public long invoke(long[] registers) {
        return value;
      }
    }

    sealed interface Combo extends OperandD17 {

      record O0() implements Combo {
        @Override
        public String print() {
          return "0";
        }

        @Override
        public long invoke(long[] registers) {
          return 0;
        }
      }

      record O1() implements Combo {
        @Override
        public String print() {
          return "1";
        }

        @Override
        public long invoke(long[] registers) {
          return 1;
        }
      }

      record O2() implements Combo {
        @Override
        public String print() {
          return "2";
        }

        @Override
        public long invoke(long[] registers) {
          return 2;
        }
      }

      record O3() implements Combo {
        @Override
        public String print() {
          return "3";
        }

        @Override
        public long invoke(long[] registers) {
          return 3;
        }
      }

      record O4() implements Combo {
        @Override
        public String print() {
          return "A";
        }

        @Override
        public long invoke(long[] registers) {
          return registers[0];
        }
      }

      record O5() implements Combo {
        @Override
        public String print() {
          return "B";
        }

        @Override
        public long invoke(long[] registers) {
          return registers[1];
        }
      }

      record O6() implements Combo {
        @Override
        public String print() {
          return "C";
        }

        @Override
        public long invoke(long[] registers) {
          return registers[2];
        }
      }

      record O7() implements Combo {
        @Override
        public String print() {
          return "wrong operator";
        }

        @Override
        public long invoke(long[] registers) {
          throw new IllegalStateException("Operand is reserved");
        }
      }
    }
  }

  public sealed interface OpcodeD17 {
    long invoke(long[] registers, int operand);

    String print(int operand);

    OpcodeD17[] opcodeCache = new OpcodeD17[]{new Adv(), new Bxl(), new Bst(), new Jnz(), new Bxc(), new Out(), new Bdv(), new Cdv()};

    record Adv() implements OpcodeD17 {
      @Override
      public long invoke(long[] registers, int operand) {
        registers[0] = registers[0] / (long) pow(2, OperandD17.getCombo(operand).invoke(registers));
        registers[3] += 2;
        return -1;
      }

      @Override
      public String print(int operand) {
        return "A = A / (2 ^ " + OperandD17.getCombo(operand).print() + ")";
      }
    }

    record Bxl() implements OpcodeD17 {
      @Override
      public long invoke(long[] registers, int operand) {
        registers[1] ^= OperandD17.literalCache[operand].value();
        registers[3] += 2;
        return -1;
      }

      @Override
      public String print(int operand) {
        return "B = B ^ " + OperandD17.literalCache[operand].print();
      }
    }

    record Bst() implements OpcodeD17 {
      @Override
      public long invoke(long[] registers, int operand) {
        registers[1] = OperandD17.getCombo(operand).invoke(registers) % 8;
        registers[3] += 2;
        return -1;
      }

      @Override
      public String print(int operand) {
        return "B = " + OperandD17.getCombo(operand).print() + " % 8";
      }
    }

    record Jnz() implements OpcodeD17 {
      @Override
      public long invoke(long[] registers, int operand) {
        if (registers[0] == 0) registers[3] += 2;
        else registers[3] = OperandD17.literalCache[operand].value();
        return -1;
      }

      @Override
      public String print(int operand) {
        return "A == 0 ? skip : JMP " + OperandD17.literalCache[operand].print();
      }
    }

    record Bxc() implements OpcodeD17 {
      @Override
      public long invoke(long[] registers, int operand) {
        registers[1] ^= registers[2];
        registers[3] += 2;
        return -1;
      }

      @Override
      public String print(int operand) {
        return "B = B ^ C";
      }
    }

    record Out() implements OpcodeD17 {
      @Override
      public long invoke(long[] registers, int operand) {
        registers[3] += 2;
        return OperandD17.getCombo(operand).invoke(registers) % 8;
      }

      @Override
      public String print(int operand) {
        return "print " + OperandD17.getCombo(operand).print() + " % 8";
      }
    }

    record Bdv() implements OpcodeD17 {
      @Override
      public long invoke(long[] registers, int operand) {
        registers[1] = registers[0] / (long) pow(2, OperandD17.getCombo(operand).invoke(registers));
        registers[3] += 2;
        return -1;
      }

      @Override
      public String print(int operand) {
        return "B = A / (2 ^ " + OperandD17.getCombo(operand).print() + ")";
      }
    }

    record Cdv() implements OpcodeD17 {
      @Override
      public long invoke(long[] registers, int operand) {
        registers[2] = registers[0] / (int) pow(2, OperandD17.getCombo(operand).invoke(registers));
        registers[3] += 2;
        return -1;
      }

      @Override
      public String print(int operand) {
        return "C = A / (2 ^ " + OperandD17.getCombo(operand).print() + ")";
      }
    }
  }

  public static List<Long> execute(List<Integer> program, long[] registers, int maxLength) {
    List<Long> results = new ArrayList<>();
    while (registers[3] < program.size()) {
      int ip = (int) registers[3];
      OpcodeD17 opcode = OpcodeD17.opcodeCache[program.get(ip)];
      int operand = program.get(ip + 1);
      long result = opcode.invoke(registers, operand);
      if (result > -1) {
        results.add(result);
        if (results.size() >= maxLength) break;
      }
    }
    return results;
  }

  public static String part1(List<String> input) {
    long a = findValue(input, "Register A");
    long b = findValue(input, "Register B");
    long c = findValue(input, "Register C");
    long[] registers = {a, b, c, 0};
    List<Integer> program = findAllValues(input);

    List<Long> results = execute(program, registers, Integer.MAX_VALUE);
    return results.stream().map(Object::toString).collect(Collectors.joining(","));
  }

  public static long part2(List<String> input) {
    var b = findValue(input, "Register B");
    var c = findValue(input, "Register C");

    var program = findAllValues(input);

    int initLength = 1;
    long a = 0;

    while (true) {
      long[] registers = {a, b, c, 0}; // A, B, C, IP
      System.out.println("Current A: " + a + ", Init Length: " + initLength);
      var result = execute(program, registers, initLength).stream().map(Long::intValue).toList();
      System.out.println("Current Result: " + result);

      if (!result.equals(program.subList(program.size() - initLength, program.size()))) {
        System.out.println("Mismatch found, incrementing A.");
        a++;
      } else {
        if (result.size() == program.size()) {
          System.out.println("Found required A: " + a);
          return a;
        }
        System.out.println("Match found but not full program, increasing Init Length.");
        initLength++;
        a <<= 3;
      }
    }
  }

  private static Long findValue(List<String> input, String startsWith) {
    return input.stream().filter(s -> s.startsWith(startsWith)).findFirst().map(s -> s.replaceAll("\\D+", "")).map(Long::parseLong).orElseThrow();
  }

  private static List<Integer> findAllValues(List<String> input) {
    return Pattern.compile("\\d+").matcher(input.stream().filter(s -> s.startsWith("Program")).findFirst().orElseThrow()).results().map(match -> Integer.parseInt(match.group())).collect(Collectors.toList());
  }

  public static void main(String[] args) throws IOException {
    List<String> input = Files.lines(Paths.get("src/17.txt")).toList();
    System.out.println(part1(input));
    System.out.println(part2(input));
  }
}