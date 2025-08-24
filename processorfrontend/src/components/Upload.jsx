import {useState} from "react";
import axios from "axios";
import '../css/upload.css'

export default function Upload({onClick, loading}) {
    const [progress, setProgress] = useState(0);
    const [status, setStatus] = useState({ message: "", success: false});

    const handleFileUpload = async (e) => {
        const file = e.target.files[0];
        if (!file || file.type !== "text/csv") {
            setStatus({ message: "Please upload a valid CSV file", success: false });
            return;
        }

        const formData = new FormData();
        formData.append("file", file);

        try {
            const response = await axios.post("http://localhost:8081/api/upload", formData, {
                headers: {"Content-Type": "multipart/form-data"},
                onUploadProgress: (progressEvent) => {
                    if (progressEvent.total) {
                        const percent = Math.round(
                            (progressEvent.loaded * 100) / progressEvent.total
                        );
                        setProgress(percent);
                        setStatus({message : `${percent}% uploaded... please wait`, success: false});
                    }
                },
            });

            setStatus({message : `${file.name} Uploaded successfully!`, success: true});
            console.log(response.data);
            console.log(`from Upload inside try ${loading}`);

            setTimeout(() => setProgress(0), 1000);
        } catch (err) {
            if (axios.isCancel(err)) {
                setStatus({message : "Upload Aborted", success: false});
            } else {
                setStatus({message : "Upload Failed", success: false});
            }
            console.error("Upload failed: ", err);
        }
        console.log(`from Upload outside try ${loading}`)
    };

    return (
        <div className="upload">
            <label className="uploadBox">
                Upload CSV file
                <input type="file" accept=".csv" onChange={handleFileUpload}/>
            </label>
            <div className="progress">
                {progress > 0 && (<div className="bar">
                    <div className="barComplete" style={{width: `${progress}%`}}/>
                    {progress}%
                </div>)}
                <p className="status">{status.message}</p>
                {loading ? (
                    <p className="status" >Analyzing... please wait</p>
                ) : (
                    status.success && (
                        <button onClick={onClick}>
                            Analyze
                        </button>
                    )
                )}
            </div>
        </div>
    );
}
