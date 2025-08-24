import Upload from "./Upload";
import {useState} from "react";
import axios from "axios";
import '../css/process.css'


export default function Process() {
    const [products, setProducts] = useState({});
    const [showUpload, setShowUpload] = useState(true);
    const [loading, setLoading] = useState(false);
    const getData = async () => {
        setLoading(true);
        try {
            const response = await axios.get("http://localhost:8081/api/process", {
                params: {max: true}
            });
            const result = await response.data;
            console.log(result);
            setProducts(result)
            setShowUpload(false);
        } catch (e) {
            console.error("Error while fetching data: ", e);
        } finally {
            setLoading(false);
        }
        console.log(`from Process ${loading}`);
    }

    return (
        <div>
            {showUpload ? (
                <div>
                    <Upload onClick={getData} loading={loading}/>
                </div>
            ) : (<div className="process">
                <div className="tableTitle"> List of products based on category</div>
                <table border="2">
                    <thead>
                    <tr>
                        <th>Category</th>
                        <th>Product Name</th>
                        <th>Quantity Sold</th>
                    </tr>
                    </thead>
                    <tbody>
                    {Object.entries(products).map(([category, product]) => (
                        <tr key={product.id}>
                            <td>{category}</td>
                            <td>{product.name}</td>
                            <td>{product.quantitySold}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>)}
        </div>
    )
}