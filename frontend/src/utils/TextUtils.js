const TextUtills = {
    notesSlice: (notes) => {
        let sliceNotes;

        if (notes.length > 125) {
            sliceNotes = notes.slice(0, 125) + '...';
        } else {
            sliceNotes = notes + '...';
        }
        return sliceNotes;
    },

    convertTime: (time) => {
        let hours = 0;
        let minutes = time;
        if (time >= 60) {
            hours = Math.floor(time / 60);
            minutes = time - hours * 60;
        }
        return [hours, minutes];
    },

    timeRender: (array) => {
        let timeText;
        const [hours, minutes] = array;
        switch (hours) {
            case 0 :
                timeText = `${minutes} mins`;
                break;
            case 1:
                timeText = `${hours} hour ${minutes} mins`;
                break;
            default:
                timeText = `${hours} hours ${minutes} mins`;
                break;
        }
        return timeText;
    }
}

export default TextUtills;