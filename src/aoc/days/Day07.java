package aoc.days;

import aoc.common.Day;
import org.jetbrains.annotations.NonNls;

import java.util.*;

/**
 * <b>Day 07 No Space Left On Device:</b><br>
 * The first small challenge is to parse the instructions and create the file system. Again I refrain from using regular
 * expressions and instead do simple string operations and created a small state machine to parse the instructions.<br>
 * The second part is to iterate through the file system which is the first application of a breadth first traversal. We
 * start with the root folder and then sequentially add all folders to the queue. The queue is processed until it is
 * empty. To prevent multiple folder size calculations we use a local size cache (memoization), as the size of a folder
 * never changes.
 */
@NonNls
public final class Day07 implements Day {
    @Override
    public Object part1(final List<String> input) {
        final var root        = createFileSystem(input);
        final var folderQueue = new LinkedList<Folder>();
        
        folderQueue.add(root);
        var totalSize = 0;
        
        while (!folderQueue.isEmpty()) {
            final var currentFolder = folderQueue.remove();
            final var size          = currentFolder.getSize();
            
            if (size <= 100_000) {
                totalSize += size;
            }
            
            folderQueue.addAll(currentFolder.getFolders());
        }
        
        return totalSize;
    }
    
    @Override
    public Object part2(final List<String> input) {
        final var root        = createFileSystem(input);
        final var folderQueue = new LinkedList<Folder>();
        var       bestFolder  = root;
        
        final int fileSystemSize = 70_000_000;
        final int updateSize     = 30_000_000;
        final int freeSize       = fileSystemSize - root.getSize();
        final int toDeleteSize   = updateSize - freeSize;
        
        folderQueue.add(root);
        
        while (!folderQueue.isEmpty()) {
            final var currentFolder = folderQueue.remove();
            final var size          = currentFolder.getSize();
            
            if (size >= toDeleteSize && size < bestFolder.getSize()) {
                bestFolder = currentFolder;
            }
            
            folderQueue.addAll(currentFolder.getFolders());
        }
        
        return bestFolder.getSize();
    }
    
    private static Folder createFileSystem(final Collection<String> input) {
        final var instructions = input.stream().skip(1).toList();
        final var root         = new Folder();
        var       currentDir   = root;
        
        for (@NonNls final var instruction : instructions) {
            if (instruction.equals("$ ls")) {
                continue;
            }
            if (instruction.equals("$ cd ..")) {
                currentDir = currentDir.getParent();
                continue;
            }
            if (instruction.startsWith("$ cd ")) {
                currentDir = currentDir.getFolders()
                                       .stream()
                                       .filter(folder -> folder.getName().equals(instruction.substring(5)))
                                       .findFirst()
                                       .orElseThrow();
                continue;
            }
            if (instruction.startsWith("dir ")) {
                currentDir.getFolders().add(new Folder(instruction.substring(4), currentDir));
                continue;
            }
            final var parts = instruction.split(" ");
            currentDir.getFiles().add(new File(parts[1], Integer.parseInt(parts[0])));
        }
        
        return root;
    }
    
    private record File(String name, int size) {
    }
    
    private static final class Folder {
        private final @NonNls String       name;
        private final         Folder       parent;
        private final         List<File>   files   = new ArrayList<>();
        private final         List<Folder> folders = new ArrayList<>();
        private               int          size    = -1;
        
        private Folder() {
            this("root", null);
        }
        
        private Folder(final String name, final Folder parent) {
            this.name = name;
            this.parent = parent;
        }
        
        final String getName() {
            return name;
        }
        
        final int getSize() {
            if (size == -1) {
                size = files.stream().mapToInt(File::size).sum() + folders.stream().mapToInt(Folder::getSize).sum();
            }
            return size;
        }
        
        final List<Folder> getFolders() {
            //noinspection AssignmentOrReturnOfFieldWithMutableType
            return folders;
        }
        
        final List<File> getFiles() {
            //noinspection AssignmentOrReturnOfFieldWithMutableType
            return files;
        }
        
        final Folder getParent() {
            return parent;
        }
    }
}
