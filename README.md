# Compress files to a certain file length

## Usage
Build the project by running the following command:
On Linux:
```sh
./gradlew clean fatJar
```
On Windows:
```sh
.\gradlew.bat clean fatJar
```
To run compression:
```sh
java -cp build/libs/* com.williamhaw.compression.Main path/to/input path/to/output maxSizeMB
```
E.g
```sh
java -cp build/libs/* com.williamhaw.compression.Main testfiles/source testfiles/compressed 1
```
To run decompression:
```sh
java -cp build/libs/* com.williamhaw.compression.Main path/to/input path/to/output
```
E.g
```sh
java -cp build/libs/* com.williamhaw.compression.Main testfiles/compressed testfiles/decompressed
```
## Approach
### Compression
1. Recursively traverse directory system
    1. When directory seen, add to JSON structure
    2. When file seen, increment counter and use as file number, add to queue and JSON structure
2. Write JSON file to output directory
3. Take file off queue
    1. Compress into buffer with specified size (e.g 1MB) and write into file with sequence number 1
    2. If there are bytes left over, write into new file with incremented sequence number
    3. Write files as (file number).compressed.(sequence number) (e.g. 1.compressed.1)
### Decompression
1. Read JSON file
2. Create directory structure
3. Add files and paths to queue
4. Read compressed files sequentially into buffer and append to decompression path
## JSON Structure
```
[
{
    "name" : "a",
    "relativePath" : "a",
    "type" : "directory"
},
{
    "name" : "b",
    "relativePath" : "b",
    "type" : "file",
    "fileNumber" : 1
},
{
    "name" : "c",
    "relativePath" : "c"
    "type" : "directory"
},
{
    "name" : "d",
    "relativePath" : "c/d"
    "type" : "file",
    "fileNumber" : 2
},
{
    "name" : "e",
    "relativePath" : "c/e"
    "type" : "file",
    "fileNumber" : 3
}
]
```

## Compressed directory structure
```
/
    metadata.json.compressed
    1.compressed.1
    1.compressed.2
    2.compressed.1
    2.compressed.2
    2.compressed.3
    3.compressed.1
```

## To Extend
1. Implement the FileCompression and FileDecompression interfaces
or
2. Extend the AbstractCompression and AbstractDecompression classes
    1. The child class must return an InputStream for writing the entire compressed contents to disk
3. Add new type to CompressionType and CompressionFactory

## Possible Extensions
1. Keep file permissions
2. Handle symlinks