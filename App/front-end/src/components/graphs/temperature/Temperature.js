import React from "react";
import Plot from 'react-plotly.js';
import "./Temperature.css"

function TemperatureGraph(){

    return (
        <div className="graph-container">
            <h2>Temperatura dos lotes</h2>
            <Plot
                data={[
                        {   x: [1, 2, 3,4,6],
                            y: [2, 6, 3,5,7],
                            type: 'scatter',
                            mode: 'lines+markers',
                            marker: {color: 'red'},
                        }
                    ]}
                useResizeHandler={true}
                style={{width:"90%",height:"95%",position:"absolute",top:"3%",zIndex:4}}
            />
        </div>
    )
}


export default TemperatureGraph;