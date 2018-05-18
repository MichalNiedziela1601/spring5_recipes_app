import React, {Component} from 'react';

export const FormErrors = ({formErrors}) =>
    <div className="formErrors">
        {Object.keys(formErrors).map((fieldName, i) => {
            if(formErrors[fieldName].length > 0){
                return (
                    <p key={i} className='alert alert-danger'>{fieldName} {formErrors[fieldName]}</p>
                )
            } else {
                return '';
            }
        })}
    </div>