import React from "react";
import Plot from 'react-plotly.js';
import "./Temperature.css"

function TemperatureGraph(){

    return (
        <div id="graph" className="graph-container">
            <h2>Temperatura dos lotes</h2>
            <Plot divId="graph"
                data={[
                        {   x: [1, 2, 3,4,6],
                            y: [2, 6, 3,5,7],
                            type: 'scatter',
                            mode: 'lines+markers',
                            marker: {color: 'red'},
                        }
                    ]}
                useResizeHandler={true}
                layout={{showlegend:true,scrollZoom:true,paper_bgcolor:"transparent",plot_bgcolor:"white"}}
                config={{scrollZoom:true}}
                style={{width:"100%",height:"100%",position:"absolute",top:"5%"}}
            />
        </div>
    )
}


export default TemperatureGraph;