import React from 'react';
import "./Symbol.css"
function Symbol(props){


    return (
        <div className='cnt-symbol'>
            <div className='img-symbol'>
                <img src={props.img}/>
            </div>
            <div className='name-symbol'>
                {props.name}
            </div>
        </div>
    )
}

export default Symbol;