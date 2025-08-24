import '../css/header.css'

export default function Header() {
    return (
        <div className="header">
            <div className="title">Parallel Processor</div>
            <p>Parallel Processor accelerates CSV data analysis by splitting files into chunks and processing them concurrently.</p>
        </div>
    )
}