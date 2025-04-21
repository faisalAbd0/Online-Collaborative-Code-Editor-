"use client";
import CodeMirror from "@uiw/react-codemirror";
import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";

export default function CodeEditor({ projectId, project }) {
    const [code, setCode] = useState("");
    const [language, setLanguage] = useState("java");
    const [output, setOutput] = useState("");
    const [files, setFiles] = useState([]);
    const [currentFile, setCurrentFile] = useState(null);
    const [newFileName, setNewFileName] = useState("");
    const [isAddingFile, setIsAddingFile] = useState(false);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState(null);

    const router = useRouter();

    useEffect(() => {
        if (project) {
            setIsLoading(false);
            setLanguage(project.language.toLowerCase());
            if (project.files && project.files.length > 0) {
                setFiles(project.files);
                setCurrentFile(project.files[0]);
                setCode(project.files[0].content || "");
            }
        }
    }, [project]);

    const handleFileChange = async (file) => {
        if (currentFile && currentFile.filename === file.filename) {
            return;
        }

        if (currentFile && code !== currentFile.content) {
            try {
                await saveFileContent(currentFile.filename, code);
            } catch (error) {
                console.error("Error saving file before switch:", error);
                setOutput(`Failed to save ${currentFile.filename}: ${error.message}`);
                return;
            }
        }

        setCurrentFile(file);
        setCode(file.content || "");
    };

    const saveFileContent = async (filename, content) => {
        const file = files.find((f) => f.filename === filename);
        if (file && file.content === content) {
            return;
        }
        try {
            const token = localStorage.getItem("token");
            if (!token) {
                router.push("/login");
                return;
            }

            const response = await fetch(`http://localhost:8082/api/code-file/${projectId}/files/${filename}`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`,
                },
                body: JSON.stringify({ content }),
            });

            if (!response.ok) {
                throw new Error(`Failed to save file: ${response.status}`);
            }

            const updatedProject = await response.json();
            setFiles(updatedProject.files || []);

            if (currentFile && currentFile.filename === filename) {
                const updatedFile = updatedProject.files.find((f) => f.filename === filename);
                setCurrentFile(updatedFile);
            }

            setOutput("File saved successfully");
            setTimeout(() => {
                if (output === "File saved successfully") {
                    setOutput("");
                }
            }, 3000);
        } catch (error) {
            console.error("Error saving file:", error);
            setOutput(`Error saving file: ${error.message}`);
        }
    };

    const addNewFile = async () => {
        if (!newFileName.trim()) {
            setOutput("Please enter a file name");
            return;
        }

        let filename = newFileName;
        if (!filename.includes(".")) {
            const extension = getFileExtension(language);
            filename = `${filename}.${extension}`;
        }

        try {
            const token = localStorage.getItem("token");
            if (!token) {
                router.push("/login");
                return;
            }

            const response = await fetch(`http://localhost:8082/api/code-file/${projectId}/files`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`,
                },
                body: JSON.stringify({
                    filename,
                    content: "",
                }),
            });

            if (!response.ok) {
                throw new Error(`Failed to add file: ${response.status}`);
            }

            const updatedProject = await response.json();
            setFiles(updatedProject.files || []);

            const newFile = updatedProject.files.find((f) => f.filename === filename);
            if (newFile) {
                setCurrentFile(newFile);
                setCode(newFile.content || "");
            }

            setNewFileName("");
            setIsAddingFile(false);
            setOutput(`File ${filename} created successfully`);
        } catch (error) {
            console.error("Error adding file:", error);
            setOutput(`Error adding file: ${error.message}`);
        }
    };

    const deleteFile = async (filename) => {
        if (!confirm(`Are you sure you want to delete ${filename}?`)) {
            return;
        }

        try {
            const token = localStorage.getItem("token");
            if (!token) {
                router.push("/login");
                return;
            }

            const response = await fetch(`http://localhost:8082/api/code-file/${projectId}/files/${filename}`, {
                method: "DELETE",
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });

            if (!response.ok) {
                throw new Error(`Failed to delete file: ${response.status}`);
            }

            const updatedProject = await response.json();
            setFiles(updatedProject.files || []);
            setOutput(`File ${filename} deleted successfully`);

            if (currentFile && currentFile.filename === filename) {
                setCurrentFile(null);
                setCode("");
            }
        } catch (error) {
            console.error("Error deleting file:", error);
            setOutput(`Error deleting file: ${error.message}`);
        }
    };

    const getFileExtension = (lang) => {
        switch (lang.toLowerCase()) {
            case "java":
                return "java";
            case "python":
                return "py";
            case "c":
                return "c";
            case "cpp":
                return "cpp";
            default:
                return "txt";
        }
    };

    const handleRun = async () => {
        if (currentFile) {
            await saveFileContent(currentFile.filename, code);
        }

        try {
            const token = localStorage.getItem("token");
            if (!token) {
                router.push("/login");
                return;
            }

            const response = await fetch("http://localhost:8081/api/code/execute", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`,
                },
                body: JSON.stringify({
                    code,
                    language: language.toLowerCase(),
                }),
            });

            if (!response.ok) {
                throw new Error(`Execution failed: ${response.status}`);
            }

            const data = await response.text();
            setOutput(data);
        } catch (error) {
            console.error("Error executing code:", error);
            setOutput("Error: " + error.message);
        }
    };

    if (isLoading) {
        return (
            <div className="flex justify-center items-center h-screen">
                <p className="text-xl">Loading project...</p>
            </div>
        );
    }

    if (error) {
        return (
            <div className="flex justify-center items-center h-screen">
                <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded max-w-md">
                    <p className="font-bold">Error</p>
                    <p>{error}</p>
                    <button
                        onClick={() => router.push("/")}
                        className="mt-2 bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600"
                    >
                        Return to Dashboard
                    </button>
                </div>
            </div>
        );
    }

    return (
        <div className="editor-container p-6 bg-gray-100 min-h-screen">
            <div className="bg-white p-4 rounded-lg shadow mb-6">
                <h2 className="text-2xl font-bold mb-2 text-center">
                    {project ? project.projectName : "Code Editor"}
                </h2>
                <div className="flex justify-center space-x-4 text-gray-600">
                    <p>Language: {project?.language}</p>
                    <p>Created: {project?.createdAt ? new Date(project.createdAt).toLocaleDateString() : ""}</p>
                    <p>Project ID: {projectId}</p>
                </div>
            </div>

            <div className="grid grid-cols-1 lg:grid-cols-4 gap-4 mb-4">
                <div className="lg:col-span-1">
                    <div className="bg-white p-4 rounded-lg shadow">
                        <div className="flex justify-between items-center mb-4">
                            <h3 className="font-medium">File Explorer</h3>
                            <button
                                onClick={() => setIsAddingFile(true)}
                                className="px-2 py-1 text-sm bg-green-500 text-white rounded hover:bg-green-600"
                            >
                                Add File
                            </button>
                        </div>

                        {isAddingFile && (
                            <div className="mb-4">
                                <input
                                    type="text"
                                    value={newFileName}
                                    onChange={(e) => setNewFileName(e.target.value)}
                                    placeholder="filename.ext"
                                    className="w-full p-2 border border-gray-300 rounded-md mb-2"
                                />
                                <div className="flex space-x-2">
                                    <button
                                        onClick={addNewFile}
                                        className="flex-1 px-3 py-1 bg-blue-500 text-white rounded-md hover:bg-blue-600"
                                    >
                                        Add
                                    </button>
                                    <button
                                        onClick={() => setIsAddingFile(false)}
                                        className="flex-1 px-3 py-1 bg-gray-500 text-white rounded-md hover:bg-gray-600"
                                    >
                                        Cancel
                                    </button>
                                </div>
                            </div>
                        )}

                        <div className="border border-gray-200 rounded-md overflow-hidden">
                            {files.length > 0 ? (
                                <ul className="divide-y divide-gray-200">
                                    {files.map((file) => (
                                        <li key={file.filename} className="hover:bg-gray-50">
                                            <div className="px-3 py-2 flex justify-between items-center">
                                                <button
                                                    onClick={() => handleFileChange(file)}
                                                    className={`text-left flex-grow truncate ${
                                                        currentFile && currentFile.filename === file.filename
                                                            ? "font-bold text-blue-600"
                                                            : ""
                                                    }`}
                                                >
                                                    {file.filename}
                                                </button>
                                                <button
                                                    onClick={() => deleteFile(file.filename)}
                                                    className="text-red-500 hover:text-red-700 ml-2"
                                                    title="Delete file"
                                                >
                                                    ×
                                                </button>
                                            </div>
                                        </li>
                                    ))}
                                </ul>
                            ) : (
                                <p className="p-3 text-gray-500 text-center">No files yet. Add your first file!</p>
                            )}
                        </div>
                    </div>
                </div>

                <div className="lg:col-span-3">
                    <div className="bg-white p-4 rounded-lg shadow mb-4">
                        <div className="flex justify-between items-center mb-2">
                            <div className="text-sm font-medium text-gray-700">
                                {currentFile ? `Editing: ${currentFile.filename}` : "No file selected"}
                            </div>
                            <select
                                value={language}
                                onChange={(e) => setLanguage(e.target.value)}
                                className="p-1 text-sm border border-gray-300 rounded-md"
                            >
                                <option value="java">Java</option>
                                <option value="python">Python</option>
                                <option value="c">C</option>
                                <option value="cpp">C++</option>
                            </select>
                        </div>

                        <div className="border border-gray-200 rounded-md overflow-hidden">
                            <CodeMirror
                                value={code}
                                options={{
                                    mode: language,
                                    lineNumbers: true,
                                    theme: "default",
                                }}
                                onChange={(value) => setCode(value)}
                                height="400px"
                            />
                        </div>

                        <div className="flex space-x-2 mt-4">
                            <button
                                onClick={handleRun}
                                className="px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600 transition"
                                disabled={!currentFile}
                            >
                                Run Code
                            </button>

                            {currentFile && (
                                <button
                                    onClick={() => saveFileContent(currentFile.filename, code)}
                                    className="px-4 py-2 bg-green-500 text-white rounded-md hover:bg-green-600 transition"
                                >
                                    Save File
                                </button>
                            )}
                        </div>
                    </div>

                    <div className="bg-white p-4 rounded-lg shadow">
                        <h3 className="font-medium mb-2">Output</h3>
                        <pre className="p-4 bg-gray-800 text-white rounded-md overflow-auto h-48">{output}</pre>
                    </div>
                </div>
            </div>
        </div>
    );
}