package farouqmustapha.pulserescue;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.MyViewHolder> {

    private List<AmbulanceRequest> requestList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, status, time;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.txtViewName);
            status = (TextView) view.findViewById(R.id.txtViewStatus);
            time = (TextView) view.findViewById(R.id.txtViewTime);
        }
    }


    public RequestAdapter(List<AmbulanceRequest> requestList) {
        this.requestList = requestList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.request_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        AmbulanceRequest ambulanceRequest = requestList.get(position);
        holder.name.setText(ambulanceRequest.getName());
        holder.status.setText(ambulanceRequest.getRequestStatus());
        holder.time.setText(ambulanceRequest.getTime());
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }
}
